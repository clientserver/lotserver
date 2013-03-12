// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.lotserver.domain;

import com.ruyicai.lotserver.domain.Coop;
import java.lang.Integer;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Coop_Roo_Entity {
    
    declare @type: Coop: @Entity;
    
    declare @type: Coop: @Table(name = "tbl_coop");
    
    @PersistenceContext(unitName = "persistenceUnit")
    transient EntityManager Coop.entityManager;
    
    @Transactional("transactionManager")
    public void Coop.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional("transactionManager")
    public void Coop.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Coop attached = Coop.findCoop(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional("transactionManager")
    public void Coop.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional("transactionManager")
    public void Coop.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional("transactionManager")
    public Coop Coop.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Coop merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Coop.entityManager() {
        EntityManager em = new Coop().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Coop.countCoops() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Coop o", Long.class).getSingleResult();
    }
    
    public static List<Coop> Coop.findAllCoops() {
        return entityManager().createQuery("SELECT o FROM Coop o", Coop.class).getResultList();
    }
    
    public static Coop Coop.findCoop(Integer id) {
        if (id == null) return null;
        return entityManager().find(Coop.class, id);
    }
    
    public static List<Coop> Coop.findCoopEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Coop o", Coop.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}