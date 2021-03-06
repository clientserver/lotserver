// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.lotserver.domain;

import com.ruyicai.lotserver.domain.TgrayUpgrade;
import java.lang.Integer;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.springframework.transaction.annotation.Transactional;

privileged aspect TgrayUpgrade_Roo_Entity {
    
    declare @type: TgrayUpgrade: @Entity;
    
    declare @type: TgrayUpgrade: @Table(name = "tgrayupgrade");
    
    @PersistenceContext(unitName = "persistenceUnit")
    transient EntityManager TgrayUpgrade.entityManager;
    
    @Transactional("transactionManager")
    public void TgrayUpgrade.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional("transactionManager")
    public void TgrayUpgrade.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            TgrayUpgrade attached = TgrayUpgrade.findTgrayUpgrade(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional("transactionManager")
    public void TgrayUpgrade.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional("transactionManager")
    public void TgrayUpgrade.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional("transactionManager")
    public TgrayUpgrade TgrayUpgrade.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TgrayUpgrade merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager TgrayUpgrade.entityManager() {
        EntityManager em = new TgrayUpgrade().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long TgrayUpgrade.countTgrayUpgrades() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TgrayUpgrade o", Long.class).getSingleResult();
    }
    
    public static List<TgrayUpgrade> TgrayUpgrade.findAllTgrayUpgrades() {
        return entityManager().createQuery("SELECT o FROM TgrayUpgrade o", TgrayUpgrade.class).getResultList();
    }
    
    public static TgrayUpgrade TgrayUpgrade.findTgrayUpgrade(Integer id) {
        if (id == null) return null;
        return entityManager().find(TgrayUpgrade.class, id);
    }
    
    public static List<TgrayUpgrade> TgrayUpgrade.findTgrayUpgradeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TgrayUpgrade o", TgrayUpgrade.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
