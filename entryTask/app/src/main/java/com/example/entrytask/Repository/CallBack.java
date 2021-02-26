package com.example.entrytask.Repository;

public abstract  class CallBack<T>{
        public abstract void returnResult(T t);
        public abstract void returnError(String message);

}
