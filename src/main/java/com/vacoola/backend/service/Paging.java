package com.vacoola.backend.service;

/**
 * Created by r.vakulenko on 23.02.2016.
 */
public class Paging {
    public final int PAGE_SIZE = 16;
    public long currentPage = 1;
    public long maxPage = 1;

    public void updatePaging(long count) {
        maxPage = count / PAGE_SIZE;
        if (count % PAGE_SIZE != 0) {
            maxPage++;
        }

        if (maxPage < currentPage) {
            currentPage = maxPage;
        }
    }
}
