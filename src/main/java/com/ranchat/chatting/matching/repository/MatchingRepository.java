package com.ranchat.chatting.matching.repository;

public interface MatchingRepository {

    void addParticipant(String participantId);

    void deleteParticipant(String participantId);
}
