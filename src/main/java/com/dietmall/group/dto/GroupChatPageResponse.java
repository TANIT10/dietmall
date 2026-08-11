package com.dietmall.group.dto;

import java.util.List;

public class GroupChatPageResponse {

    private final List<GroupChatMessageResponse> messages;

    private final int page;
    private final int size;

    private final long totalElements;
    private final int totalPages;

    private final boolean last;


    public GroupChatPageResponse(
            List<GroupChatMessageResponse> messages,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean last) {

        this.messages = messages;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }


    public List<GroupChatMessageResponse> getMessages() {
        return messages;
    }


    public int getPage() {
        return page;
    }


    public int getSize() {
        return size;
    }


    public long getTotalElements() {
        return totalElements;
    }


    public int getTotalPages() {
        return totalPages;
    }


    public boolean isLast() {
        return last;
    }
}