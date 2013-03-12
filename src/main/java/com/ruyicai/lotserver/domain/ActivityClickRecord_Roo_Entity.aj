// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.lotserver.domain;

import com.ruyicai.lotserver.domain.ActivityClickRecord;
import java.lang.Integer;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ActivityClickRecord_Roo_Entity {
    
    declare @type: ActivityClickRecord: @Entity;
    
    declare @type: ActivityClickRecord: @Table(name = "activityclickrecord");
    
    @PersistenceContext(unitName = "persistenceUnit")
    transient EntityManager ActivityClickRecord.entityManager;
    
    @Transactional("transactionManager")
    public void ActivityClickRecord.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional("transactionManager")
    public void ActivityClickRecord.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ActivityClickRecord attached = ActivityClickRecord.findActivityClickRecord(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional("transactionManager")
    public void ActivityClickRecord.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional("transactionManager")
    public void ActivityClickRecord.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional("transactionManager")
    public ActivityClickRecord ActivityClickRecord.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ActivityClickRecord merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager ActivityClickRecord.entityManager() {
        EntityManager em = new ActivityClickRecord().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long ActivityClickRecord.countActivityClickRecords() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ActivityClickRecord o", Long.class).getSingleResult();
    }
    
    public static List<ActivityClickRecord> ActivityClickRecord.findAllActivityClickRecords() {
        return entityManager().createQuery("SELECT o FROM ActivityClickRecord o", ActivityClickRecord.class).getResultList();
    }
    
    public static ActivityClickRecord ActivityClickRecord.findActivityClickRecord(Integer id) {
        if (id == null) return null;
        return entityManager().find(ActivityClickRecord.class, id);
    }
    
    public static List<ActivityClickRecord> ActivityClickRecord.findActivityClickRecordEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ActivityClickRecord o", ActivityClickRecord.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}