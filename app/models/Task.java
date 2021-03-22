package models;

import java.util.*;

import io.ebean.Finder;
import play.data.*;
//import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;
import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Task extends Model{
    //Outlines the fields for the model that defines a Task; all fields must have values
    @Id
    public int id;
    @Required
    public String title;
    @Required
    public boolean completed;


    //Finder function that provides model updating methods to a Task
    public static Finder<Integer, Task> find = new Finder<>(Task.class);

}