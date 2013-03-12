package com.ruyicai.lotserver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import com.ruyicai.lotserver.controller.RequestDispatcher;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.util.parse.RequestParamUtil;

public class SendRequestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Logger businessLogger = Logger.getLogger("business");
	private Logger logger = Logger.getLogger(SendRequestServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String parameter = request.getParameter("parameter"); // 参数
			String callBackMethod = request.getParameter("callBackMethod"); // 回调客户端的方法名
			// 打印参数
			businessLogger.info(parameter);
			// 解析请求参数
			ClientInfo clientInfo = new ClientInfo();
			JSONObject requestObject = JSONObject.fromObject(parameter);
			RequestParamUtil.parseRequestParam(request, requestObject,
					clientInfo);
			// 请求lotserver
			String returnToClient = RequestDispatcher.dispatch(clientInfo);
			returnToClient = callBackMethod + "(" + returnToClient + ")";
			// 返回
			response.setHeader("Pragma", "no-cache");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(returnToClient);
		} catch (Exception e) {
			logger.error("SendRequestServlet发生异常", e);
		}
	}

}
