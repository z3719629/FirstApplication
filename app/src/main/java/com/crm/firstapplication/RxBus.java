package com.crm.firstapplication;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import com.crm.firstapplication.events.Events;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.reactivestreams.Subscription;

/**
 * Created by Administrator on 2017/12/27.
 */
public class RxBus {

    private static volatile RxBus rxBus;
    private final Subject<Events<?>> _bus = PublishSubject.<Events<?>>create().toSerialized();

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

    public void send(Events<?> o) {
        _bus.onNext(o);
    }

    public void send(@Events.EventCode int code, Object content){
        Events<Object> event = new Events<>();
        event.code = code;
        event.content = content;
        send(event);
    }

    public Subject<Events<?>> getBus() {
        return _bus;
    }

    public <T> Observable tObservable(final Class<T> eventType) {
        return _bus.ofType(eventType.getClass());//判断接收事件类型
    }

}
