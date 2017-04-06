package com.cn.aixiyi.mode;

/**
 * Created by xingdapeng on 2016/10/27.
 */
public class BodyXyContact {
    //警报alarms
    public final static String Alarms = "{\"source\": {\"id\": \"10200\" },\"type\": \"TestAlarm\",\"text\": \"I am an alarm\",\"severity\": \"MINOR\",\"status\": \"ACTIVE\",\"time\": \"2014-03-03T12:03:27.845Z\"}";
    //警报alarmsalarmId
    public final static String AlarmsAlarmId ="{\n" + "\"status\": \"ACTIVE\"\n" + "}";
    //notifications
    public final static String Notifications ="[\n" + "\t{\n" + "\t\"version\":\"1.0\",\n" + "\"minimumVersion\":\"0.9\",\n" + "\"channel\":\"/meta/handshake\",\n" + "\"supportedConnectionTypes\":[\"long-polling\"],\n" + "\"advice\":{\n" + "\t\"timeout\":60000,\"interval\":0\n" + "}\n" + "}\n" + "]";
    //s
    public final static String S ="11,500,,$.c8y_IsDevice,$.id,$.c8y_Configuration.config\n" + "11,501,,$.externalId,$.managedObject.id\n" + "11,502,,$.severity,$.id\n" + "11,503,$.operations,$.c8y_Configuration,$.id,$.c8y_Configuration.config" ;
   //module
    public final static String Module ="{\n" + "\"status\": \"DEPLOYED\"\n" + "}" ;
    //cep
    public final static String Cep ="module SMSTESTSINGLE; \n" + "\n" + "insert into SendSms \n" + "select \n" + "  \"+49123456789\" as receiver, \n" + "  \"Hello World\" as text \n" + "from AlarmCreated a \n" + "where a.type = \"smsSingleTestAlarm\";";
   //user
   public final static String User="{\n" +
           "\t\"enabled\":true,\n" +
           "    \"userName\":\"xiaoya4\",\n" +
           "    \"firstName\":\"xiao4\",\n" +
           "    \"lastName\":\"ya4\",\n" +
           "    \"password\":\"1234QWER4\"\n" +
           "}";
}
