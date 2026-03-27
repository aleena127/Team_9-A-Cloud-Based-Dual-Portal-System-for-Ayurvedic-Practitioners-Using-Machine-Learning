//Client.java
package com.ayur;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String password;
    private String role; 
    private String medicineBranch; 
    private String prakriti;
    private String agniStrength;
    private String currentRoom;
    private String progressNotes;
    private String abhaId;
    private String primaryDosha;
    private String prakritiQuizResult;

    public Client() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getMedicineBranch() { return medicineBranch; }
    public void setMedicineBranch(String medicineBranch) { this.medicineBranch = medicineBranch; }
    public String getPrakriti() { return prakriti; }
    public void setPrakriti(String prakriti) { this.prakriti = prakriti; }
    public String getAgniStrength() { return agniStrength; }
    public void setAgniStrength(String agniStrength) { this.agniStrength = agniStrength; }
    public String getCurrentRoom() { return currentRoom; }
    public void setCurrentRoom(String currentRoom) { this.currentRoom = currentRoom; }
    public String getProgressNotes() { return progressNotes; }
    public void setProgressNotes(String progressNotes) { this.progressNotes = progressNotes; }
    public String getAbhaId() { return abhaId; }
    public void setAbhaId(String abhaId) { this.abhaId = abhaId; }
    public String getPrimaryDosha() { return primaryDosha; }
    public void setPrimaryDosha(String primaryDosha) { this.primaryDosha = primaryDosha; }
    public String getPrakritiQuizResult() { return prakritiQuizResult; }
    public void setPrakritiQuizResult(String prakritiQuizResult) { this.prakritiQuizResult = prakritiQuizResult; }
}