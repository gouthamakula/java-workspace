package com.modules.hateoas;

import org.springframework.transaction.support.ResourceHolderSupport;

public class BookResource extends ResourceHolderSupport {
    private long isbn;
    private String title;

    public BookResource(long isbn, String title) {
        this.isbn = isbn;
        this.title = title;
    }

}
