package com.taohdao.widget.comment;

public abstract class AbsCommentImpl implements IComment {
    public abstract String parentUserId();
    public abstract String childUserId();
}
