package dao;

import models.Model;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by alec on 10/5/16.
 */
public interface Dao<T extends Model> {

    List<T> findAll();

    T create(T model) throws PersistenceException;
}