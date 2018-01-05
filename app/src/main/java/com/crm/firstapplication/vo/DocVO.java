package com.crm.firstapplication.vo;

import com.crm.firstapplication.pojo.Doc;

public class DocVO {

    Doc doc;

    public DocVO() {
    }

    public DocVO(Doc doc) {
        this.doc = doc;
    }

    public Doc getDoc() {
        return doc;
    }

    public void setDoc(Doc doc) {
        this.doc = doc;
    }
}
