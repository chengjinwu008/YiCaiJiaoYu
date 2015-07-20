package com.cjq.yicaijiaoyu.dao;

import java.util.List;
import com.cjq.yicaijiaoyu.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table HISTORY.
 */
public class History {

    private Long id;
    private String name;
    private Integer authority;
    private String image;
    private String userId;
    private Long order;
    private Boolean cared;
    private Boolean bought;
    private String categoryName;
    private String goods_id;
    private String info;

    public void setLectureList(List<Lecture> lectureList) {
        this.lectureList = lectureList;
    }

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient HistoryDao myDao;

    private List<Lecture> lectureList;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public History() {
    }

    public History(Long id) {
        this.id = id;
    }

    public History(Long id, String name, Integer authority, String image, String userId, Long order, Boolean cared, Boolean bought, String categoryName, String goods_id, String info) {
        this.id = id;
        this.name = name;
        this.authority = authority;
        this.image = image;
        this.userId = userId;
        this.order = order;
        this.cared = cared;
        this.bought = bought;
        this.categoryName = categoryName;
        this.goods_id = goods_id;
        this.info = info;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getHistoryDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAuthority() {
        return authority;
    }

    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Boolean getCared() {
        return cared;
    }

    public void setCared(Boolean cared) {
        this.cared = cared;
    }

    public Boolean getBought() {
        return bought;
    }

    public void setBought(Boolean bought) {
        this.bought = bought;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Lecture> getLectureList() {
        if (lectureList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LectureDao targetDao = daoSession.getLectureDao();
            List<Lecture> lectureListNew = targetDao._queryHistory_LectureList(goods_id);
            synchronized (this) {
                if(lectureList == null) {
                    lectureList = lectureListNew;
                }
            }
        }
        return lectureList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetLectureList() {
        lectureList = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
