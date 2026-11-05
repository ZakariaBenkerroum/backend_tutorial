package com.ccsw.tutorial.Loan.model;

import com.ccsw.tutorial.common.pagination.PageableRequest;

public class LoanSearchDto {

    private PageableRequest pageable;

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }
}
