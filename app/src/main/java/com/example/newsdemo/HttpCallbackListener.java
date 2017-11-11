package com.example.newsdemo;

/**
 * @version $Rev$
 * @anthor Administrator
 * @dsc ${TOOD}
 * @updateAuthor $Author
 * @updateDsc ${TOOD}
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
