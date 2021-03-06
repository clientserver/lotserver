// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.lotserver.domain;

import com.ruyicai.lotserver.domain.SecurityCode;
import java.lang.Integer;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SecurityCode_Roo_Entity {
    
    declare @type: SecurityCode: @Entity;
    
    declare @type: SecurityCode: @Table(name = "vol_securitycode");
    
    @PersistenceContext(unitName = "persistenceUnit")
    transient EntityManager SecurityCode.entityManager;
    
    @Transactional("transactionManager")
    public void SecurityCode.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional("transactionManager")
    public void SecurityCode.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            SecurityCode attached = SecurityCode.findSecurityCode(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional("transactionManager")
    public void SecurityCode.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional("transactionManager")
    public void SecurityCode.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional("transactionManager")
    public SecurityCode SecurityCode.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SecurityCode merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager SecurityCode.entityManager() {
        EntityManager em = new SecurityCode().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long SecurityCode.countSecurityCodes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SecurityCode o", Long.class).getSingleResult();
    }
    
    public static List<SecurityCode> SecurityCode.findAllSecurityCodes() {
        return entityManager().createQuery("SELECT o FROM SecurityCode o", SecurityCode.class).getResultList();
    }
    
    public static SecurityCode SecurityCode.findSecurityCode(Integer id) {
        if (id == null) return null;
        return entityManager().find(SecurityCode.class, id);
    }
    
    public static List<SecurityCode> SecurityCode.findSecurityCodeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SecurityCode o", SecurityCode.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
