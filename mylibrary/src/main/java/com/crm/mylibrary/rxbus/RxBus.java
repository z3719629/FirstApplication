package com.crm.mylibrary.rxbus;

import android.util.Log;

import com.crm.mylibrary.contract.BaseContract;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Administrator on 2017/12/27.
 */
public class RxBus {

    private static volatile RxBus rxBus;
    private final Subject<Events<BaseContract.BaseInterface>> _bus = PublishSubject.<Events<BaseContract.BaseInterface>>create().toSerialized();

    private RxBus(){}

    public static RxBus getInstance(){
        if (rxBus == null){
            synchronized (RxBus.class){
                if (rxBus == null){
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    public void send(Events<BaseContract.BaseInterface> o) {
        Log.i("DDDDDDDDDDDD", "onNext");
        _bus.onNext(o);
    }

    public void send(BaseContract.BaseInterface target, @Events.EventCode int code, Object content){
        Map<String, Object> map = new ConcurrentHashMap();
        Events<BaseContract.BaseInterface> event = new Events<>();
        event.code = code;
        map.put("content", content);
        event.setContent(map);
        event.setTarget(target);
        send(event);
    }

    public Subject<Events<BaseContract.BaseInterface>> getBus() {
        return _bus;
    }

    public <T> Observable tObservable(final Class<T> eventType) {
        return _bus.ofType(eventType.getClass());//判断接收事件类型
    }

}
