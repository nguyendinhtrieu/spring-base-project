package com.tzyel.springbaseproject.integration;

public interface DatabaseAlias {
    interface ProductTable {
        String NAME = "product";

        interface Column {
            String ID = "id";
            String NAME = "name";
            String COMPANY_ID = "company_id";
            String NOTE = "note";
            String CREATED_AT = "created_at";
            String UPDATED_AT = "updated_at";
        }
    }

    interface CompanyTable {
        String NAME = "company";

        interface Column {
            String ID = "id";
            String NAME = "name";
            String NOTE = "note";
            String CREATED_AT = "created_at";
            String UPDATED_AT = "updated_at";
        }
    }
}
