package com.bfd.note.store;

import java.util.List;

public interface Query {
    /**
     * return the query result form container
     * @param keyWord key word
     * @return lists of id, order by relevance
     */
    List<Long> query(String keyWord);
}
