package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.api.i18n.Messages;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;

import javax.inject.*;
import play.api.i18n.I18nSupport;
import play.data.Form;
import models.*;
import play.data.FormFactory;
import utils.Util;

import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */

public class HomeController extends Controller {

    //final Logger log = LoggerFactory.getLogger(this.getClass());
    private Task task;
    private HttpExecutionContext executionContext;

    @Inject
    public HomeController(HttpExecutionContext executionContext, Task task){
        this.task = task;
        this.executionContext = executionContext;
    }

    public Result index() {
        List<Task> tasks = Task.find.all();
        return ok(Json.toJson(tasks));
    }

    public Result tasks(){
        List<Task> tasks = Task.find.all();
        return ok(Json.toJson(tasks)).as("application/json");
    }

    public Result newTask(Http.Request request){
        JsonNode json = request.body().asJson();
        if (json == null){
            return badRequest(Util.createResponse("Expecting JSON data", false));
        }

        String label = json.findPath("title").textValue();
        boolean completed = json.findPath("completed").booleanValue();
        if (label == null){
            return badRequest(Util.createResponse("Missing label", false));
        }
        else {
            Task task = new Task();
            task.title = label;
            task.completed = completed;
            task.save();

            return ok(Json.toJson(task));
        }
    }

    public Result deleteTask(int id){
        Task task = Task.find.byId(id);

        if (task == null){
            return notFound(Util.createResponse("Task# " + id + "does not exist", false));
        }

        task.delete();
        return redirect(routes.HomeController.tasks());
    }

    public Result updateTask(Http.Request request){
        JsonNode json = request.body().asJson();
        if (json == null){
            return badRequest(Util.createResponse("Expecting JSON data", false));
        }
        int id = json.findPath("id").intValue();
        String newlabel = json.findPath("title").textValue();
        boolean newcompleted = json.findPath("completed").booleanValue();
        Task task = Task.find.byId(id);
        task.title = newlabel;
        task.completed = newcompleted;
        task.update();
        List<Task> tasks = Task.find.all();

        return ok(Json.toJson(tasks));
    }



}
