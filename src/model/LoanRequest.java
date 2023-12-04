/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Farid
 */
public class LoanRequest {
    private int requestId;
    private User borrower;
    private User lender;
    private Book bookRequested;
    private String requestStatus; 

    public LoanRequest(int requestId, User borrower, User lender, Book bookRequested, String requestStatus) {
        this.requestId = requestId;
        this.borrower = borrower;
        this.lender = lender;
        this.bookRequested = bookRequested;
        this.requestStatus = requestStatus;
    }

    public int getRequestId() {
        return requestId;
    }

    public User getBorrower() {
        return borrower;
    }

    public User getLender() {
        return lender;
    }

    public Book getBookRequested() {
        return bookRequested;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public void setLender(User lender) {
        this.lender = lender;
    }

    public void setBookRequested(Book bookRequested) {
        this.bookRequested = bookRequested;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
    
    
}
