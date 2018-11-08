package com.gzz100.zbh.data;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Lam on 2018/7/10.
 */

public abstract class ObserverImpl<T> implements Observer<T> {
    Disposable disposable;
    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(T t) {
        onResponse(t);
    }

    @Override
    public void onError(Throwable e) {
        onFailure(e);
    }

    @Override
    public void onComplete() {

    }

    public void cancleRequest(){
        if (disposable!=null&&!disposable.isDisposed()){
            disposable.dispose();
        }
    }

    protected abstract void onResponse(T t);

    protected abstract void onFailure(Throwable e);

}
