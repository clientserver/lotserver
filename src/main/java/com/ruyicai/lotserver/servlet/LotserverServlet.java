package com.ruyicai.lotserver.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.controller.RequestDispatcher;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.common.ToolsAesCrypt;
import com.ruyicai.lotserver.util.parse.RequestParamUtil;

public class LotserverServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Logger businessLogger = Logger.getLogger("business");
	private Logger logger = Logger.getLogger(LotserverServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			StringBuffer sb = new StringBuffer();
			InputStream in = request.getInputStream();
			byte[] buf = new byte[1024];
			int i = in.read(buf);
			while (i != -1) {
				sb.append(new String(buf, 0, i));
				i = in.read(buf);
			}
			in.close();
			String deCrypt = null;
			String isEncrypt = request.getParameter("isEncrypt"); // 是否加密(0:不加密)
			try {
				if (isEncrypt != null && isEncrypt.equals("0")) { // 不加密
					deCrypt = sb.toString();
				} else {
					deCrypt = ToolsAesCrypt.Decrypt(sb.toString(), Constants.key);
				}
			} catch (Exception e) {
				logger.error("解密发生异常", e);
			}
			// 打印参数
			businessLogger.info(deCrypt);
			// 解析请求参数
			ClientInfo clientInfo = new ClientInfo();
			JSONObject requestObject = JSONObject.fromObject(deCrypt);
			RequestParamUtil.parseRequestParam(request, requestObject, clientInfo);
			// 请求lotserver
			String returnToClient = RequestDispatcher.dispatch(clientInfo);
			// 压缩
			String isCompress = clientInfo.getIsCompress(); // 是否压缩
			if (isCompress != null && isCompress.equals("1")) { // 压缩
				String compressString = "";
				try {
					byte[] compressBytes = Tools.compress(returnToClient
							.getBytes("UTF-8"));
					compressString = Tools.base64Encode(compressBytes, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					logger.error("压缩发生异常", e);
				}
				returnToClient = compressString; // 将字符串进行压缩
			}
			// 加密
			try {
				if (isEncrypt == null || !isEncrypt.equals("0")) { // 加密
					returnToClient = ToolsAesCrypt.Encrypt(returnToClient,
							Constants.key);
				}

			} catch (Exception e) {
				logger.error("加密发生异常", e);
			}
			// 返回
			response.setHeader("Pragma", "no-cache");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(returnToClient);
		} catch (Exception e) {
			logger.error("LotserverServlet发生异常", e);
		}
	}

}
