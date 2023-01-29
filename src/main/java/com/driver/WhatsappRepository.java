package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public class WhatsappRepository {
    private HashMap<String,User> userHashMap;
    private HashMap<Integer,Message> messageHashMap;
    private HashMap<User, List<Message>> messageUserHashMap;
    private HashMap<Group,List<Message>> groupHashMap;
    private HashMap<Group,List<User>> groupUserHashMap;
    private int groupCount=0;

    public WhatsappRepository() {
        this.userHashMap = new HashMap<>();
        this.messageHashMap = new HashMap<>();
        this.messageUserHashMap = new HashMap<>();
        this.groupHashMap = new HashMap<>();
        this.groupUserHashMap = new HashMap<>();
    }

    public String createUser(String name,String mobile) throws Exception{
        //If the mobile number exists in database, throw "User already exists" exception
        //Otherwise, create the user and return "SUCCESS"
      if(userHashMap.containsKey(mobile)){
          throw new Exception("User already Exist");
      }
      User user=new User(name,mobile);
      userHashMap.put(mobile,user);
      return "SUCCESS";
    }
    public Group createGroup(List<User> users){
        // The list contains at least 2 users where the first user is the admin. A group has exactly one admin.
        // If there are only 2 users, the group is a personal chat and the group name should be kept as the name of the second user(other than admin)
        // If there are 2+ users, the name of group should be "Group count". For example, the name of first group would be "Group 1", second would be "Group 2" and so on.
        // Note that a personal chat is not considered a group and the count is not updated for personal chats.
        // If group is successfully created, return group.

        //For example: Consider userList1 = {Alex, Bob, Charlie}, userList2 = {Dan, Evan}, userList3 = {Felix, Graham, Hugh}.
        //If createGroup is called for these userLists in the same order, their group names would be "Group 1", "Evan", and "Group 2" respectively.

        String groupName = null;
        if(users.size()>2) {
            groupName = "Group "+ ++groupCount;
        }
        else {
            groupName = users.get(1).getName();
        }
        Group group = new Group(groupName, users.size());
        groupUserHashMap.put(group, users);
        return group;

    }
    public int createMessage(String content){
        // The 'i^th' created message has message id 'i'.
        // Return the message id.
        int messageId=1;
        Message message=new Message(messageId,content,new Date());
        messageHashMap.put(messageId,message);
        return messageId;
    }
    public  int sendMessage(Message message, User sender, Group group)throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "You are not allowed to send message" if the sender is not a member of the group
        //If the message is sent successfully, return the final number of messages in that group.
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
        if(senderFound==false){
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
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        //Throw "User is not a participant" if the user is not a part of the group
        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.

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
        //A user belongs to exactly one group
        //If user is not found in any group, throw "User not found" exception
        //If user is found in a group and it is the admin, throw "Cannot remove admin" exception
        //If user is not the admin, remove the user from the group, remove all its messages from all the databases, and update relevant attributes accordingly.
        //If user is removed successfully, return (the updated number of users in the group + the updated number of messages in group + the updated number of overall messages)
       return 0;

       }

    public String findMessage(Date start, Date end, int K) throws Exception{return "pending";}

}
