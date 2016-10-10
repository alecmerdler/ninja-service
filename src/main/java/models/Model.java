package models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by alec on 10/5/16.
 */
@MappedSuperclass
public abstract class Model {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    public Model() {

    }

    public Long getId() {
        return id;
    }
}
