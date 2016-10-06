package models;

import dao.Dao;

import javax.persistence.Transient;

/**
 * Created by alec on 10/5/16.
 */
public class Model {
    @Transient
    private static Dao manager;

    public Model() {

    }

    public Model(Dao dao) {
        this.manager = dao;
    }

    public static Dao getManager() {
        return manager;
    }
}
