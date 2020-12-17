import com.google.gson.Gson;
import testoptimal.api.*;
import testoptimal.api.FSM.Model;
import testoptimal.api.FSM.State;
import testoptimal.api.FSM.Trans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class Main {


    public static void main(String[] args) throws APIError, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {

        // create model
        String modelName="digikalaRestOptiamlFullTest";
        Model model = new Model(modelName);

        State Start=model.addStateInitial("Start");
        State End=model.addStateFinal("End");
        State s_addedMobileToMyBasket=model.addState("s_addedMobileToMyBasket");
        State s_digikalaSite=model.addState("s_digikalaSite");
        State s_isLGTVSearched=model.addState("s_isLGTVSearched");
        State s_isMobileSearched=model.addState("s_isMobileSearched");
        State s_isTVSearched=model.addState("s_isTVSearched");
        State s_mobileDetailPage=model.addState("s_mobileDetailPage");
        State s_TVDetailPage=model.addState("s_TVDetailPage");
        State s_emptyBasket=model.addState("s_emptyBasket");

        s_addedMobileToMyBasket.addTrans("t_close",End);
        s_digikalaSite.addTrans("t_searchMobile",s_isMobileSearched);
        s_digikalaSite.addTrans("t_TVSearch",s_isTVSearched);
        s_isLGTVSearched.addTrans("t_LGTVDetail",s_TVDetailPage);
        s_isMobileSearched.addTrans("t_mobileDetail",s_mobileDetailPage);
        s_isTVSearched.addTrans("t_LGTVSearch",s_isLGTVSearched);
        s_mobileDetailPage.addTrans("t_addToBasket",s_addedMobileToMyBasket);
        s_TVDetailPage.addTrans("t_gotoHomePage",s_digikalaSite);
        Start.addTrans("t_gotoDigikalaSite",s_digikalaSite);
        s_addedMobileToMyBasket.addTrans("t_removeMobileFromBasket",s_emptyBasket);
        s_emptyBasket.addTrans("t_backToDetail",s_mobileDetailPage);




        // generate test cases from the model
        Server svr = new Server(Server.Protocol.http, "localhost", 8888, "USERName", "Password");
        ModelAPI modelApi = svr.getModelAPI();
        modelApi.upload(model);

        RunResult result = modelApi.genPaths(model.getName(), Constants.MbtMode.Optimal);
        System.out.println(result);



        //run model
        AgentAPI agentAPI = svr.getAgentAPI();
        agentAPI.startModel(modelName);
        agentAPI.regAgent(modelName);
        String cmd = agentAPI.getNextCmd();
        Class<?> c = Class.forName("digikala.DigiKalaTestClass");
        Object digiKalaTestClass = c.newInstance();

        while (cmd != null && !cmd.equals("")) {
            System.out.println("Next Cmd: " + cmd);
            Method method=digiKalaTestClass.getClass().getMethod(cmd.split(":")[1].trim());
            method.invoke(digiKalaTestClass);
            agentAPI.setResult(true, "Echo " + cmd);
            cmd = agentAPI.getNextCmd();

        }
        System.out.println ("Agent is done");
        RunResult runResult = agentAPI.getModelStats(modelName);
        System.out.println("results: " + runResult.toString());





    }


}
