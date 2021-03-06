// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.lotserver.domain;

import com.ruyicai.lotserver.domain.GiftMessage;
import java.lang.Integer;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.springframework.transaction.annotation.Transactional;

privileged aspect GiftMessage_Roo_Entity {
    
    declare @type: GiftMessage: @Entity;
    
    declare @type: GiftMessage: @Table(name = "giftmessage");
    
    @PersistenceContext(unitName = "persistenceUnit")
    transient EntityManager GiftMessage.entityManager;
    
    @Transactional("transactionManager")
    public void GiftMessage.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional("transactionManager")
    public void GiftMessage.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            GiftMessage attached = GiftMessage.findGiftMessage(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional("transactionManager")
    public void GiftMessage.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional("transactionManager")
    public void GiftMessage.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional("transactionManager")
    public GiftMessage GiftMessage.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        GiftMessage merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager GiftMessage.entityManager() {
        EntityManager em = new GiftMessage().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long GiftMessage.countGiftMessages() {
        return entityManager().createQuery("SELECT COUNT(o) FROM GiftMessage o", Long.class).getSingleResult();
    }
    
    public static List<GiftMessage> GiftMessage.findAllGiftMessages() {
        return entityManager().createQuery("SELECT o FROM GiftMessage o", GiftMessage.class).getResultList();
    }
    
    public static GiftMessage GiftMessage.findGiftMessage(Integer id) {
        if (id == null) return null;
        return entityManager().find(GiftMessage.class, id);
    }
    
    public static List<GiftMessage> GiftMessage.findGiftMessageEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM GiftMessage o", GiftMessage.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
