package com.ranchat.chatting.matching.repository;

import java.util.List;

public interface MatchingRepository {

    List<String> findWaitingUsers();

    void addUser(String userId);

    void deleteUsers(List<String> userId);

    void deleteUser(String userId);
}
