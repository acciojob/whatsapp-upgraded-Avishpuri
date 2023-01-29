package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class WhatsappRepository {
     HashMap<String,User>userHashMap=new HashMap<>();
     Map<Integer,Message> messageHashMap=new LinkedHashMap<>();
     HashMap<User, List<Message>> messageUserHashMap=new HashMap<>();
     HashMap<Group,List<Message>> groupHashMap=new HashMap<>();
     HashMap<Group,List<User>> groupUserHashMap=new HashMap<>();

     int groupCount=0;
     int i=0;
    public String addUser(String name,String mobile) throws Exception{

      if(userHashMap.containsKey(mobile)){
          throw new Exception("User already Exist");
      }
      User user=new User(name,mobile);
      userHashMap.put(mobile,user);
      return "SUCCESS";
    }
    public Group createGroup(List<User> users){

        String groupName;

        if(users.size()>2) {
            groupName = "Group "+ ++groupCount;
        }
        else{
            groupName = users.get(1).getName();
        }
        Group group = new Group(groupName, users.size());
        groupUserHashMap.put(group, users);
        return group;

    }
    public int createMessage(String content){

        int messageId=++i;
        Message message=new Message(messageId,content,new Date());
        messageHashMap.put(messageId,message);
        return messageId;
    }
    public  int sendMessage(Message message, User sender, Group group)throws Exception{

        if(!groupUserHashMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        boolean senderFound=false;
        for(User user:groupUserHashMap.get(group)){
            if(user==sender){
                senderFound=true;
                break;
            }
        }
        if(!senderFound){
            throw new Exception("You are not allowed to send message");
        }
        if(groupHashMap.containsKey(group)) {
            groupHashMap.get(group).add(message);
        }
        else {
            List<Message> messageList = new ArrayList<>();
            messageList.add(message);
            groupHashMap.put(group, messageList);
        }

        if(messageUserHashMap.containsKey(sender)) {
            messageUserHashMap.get(sender).add(message);
        }
        else {
            List<Message> messageList = new ArrayList<>();
            messageList.add(message);
            messageUserHashMap.put(sender, messageList);
        }

        return groupHashMap.get(group).size();

    }


    public String changeAdmin(User approver, User user, Group group) throws Exception{

        if(!groupUserHashMap.containsKey(group)) {
        throw new Exception("Group does not exist");
    }
        if(approver!=groupUserHashMap.get(group).get(0)) {
            throw new Exception("Approver does not have rights");
        }
        boolean userFound = false;
        for(User user1 : groupUserHashMap.get(group)) {
            if(user==user1) {
                userFound = true;
                break;
            }
        }
        if(!userFound) {
            throw new Exception("User is not a participant");
        }

        List<User> userList = groupUserHashMap.get(group);
        userList.remove(user);
        userList.set(0,user);
        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception {

        Group group =  null;
        for(Group group1 : groupUserHashMap.keySet()) {
            for (User user1 : groupUserHashMap.get(group1)) {
                if(user==user1) {
                    if(groupUserHashMap.get(group1).get(0)==user) {
                        throw new Exception("Cannot remove admin");
                    }
                    group = group1;
                    break;
                }
            }
        }
        if(group==null) {
            throw new Exception("User not found");
        }
        for(Message message : messageUserHashMap.get(user)) {
            messageHashMap.remove(message.getId());
            groupHashMap.get(group).remove(message);
        }
        messageUserHashMap.remove(user);
        groupUserHashMap.get(group).remove(user);
        userHashMap.remove(user.getMobile());
        return groupUserHashMap.get(group).size()+groupHashMap.get(group).size()+messageHashMap.size();

       }

    public String findMessage(Date start, Date end, int K) throws Exception{

        if(K==0){
            throw new Exception();
        }
        return "pending";
    }

}
