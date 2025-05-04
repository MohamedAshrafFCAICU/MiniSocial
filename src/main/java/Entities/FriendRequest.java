package Entities;

import Enums.RequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Friend_Requests")
public class FriendRequest extends _BaseEntity
{
    @NotNull
    @Column(nullable = false , name = "request_status")
    @Enumerated(EnumType.STRING)
    private RequestStatus request = RequestStatus.PENDING;

    // Navigational Properties:
    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id" , nullable = false)
    private User sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id" , nullable = false)
    private User receiver;


    public RequestStatus getRequest() {
        return request;
    }

    public void setRequest(RequestStatus request) {
        this.request = request;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }


}
