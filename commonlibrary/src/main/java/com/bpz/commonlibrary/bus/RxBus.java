package com.bpz.commonlibrary.bus;

import org.reactivestreams.Subscriber;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Administrator on 2018/4/2.
 * rxBus封装事件传递
 * 参考：https://www.jianshu.com/p/71ab00a2677b
 */

public class RxBus {
    /**
     * 在Android开发中，Sticky事件只指事件消费者在事件发布之后才注册的也能接收到该事件的特殊类型。
     * Android中就有这样的实例，也就是Sticky Broadcast，即粘性广播。
     * 正常情况下如果发送者发送了某个广播，而接收者在这个广播发送后才注册自己的Receiver，这时接收者便无法接收到刚才的广播，
     * 为此Android引入了StickyBroadcast，在广播发送结束后会保存刚刚发送的广播（Intent），
     * 这样当接收者注册完Receiver后就可以接收到刚才已经发布的广播。
     * 这就使得我们可以预先处理一些事件，让有消费者时再把这些事件投递给消费者。
     * <p>
     * PublishSubject只会给在订阅者订阅的时间点之后的数据发送给观察者。
     * BehaviorSubject在订阅者订阅时，会发送其最近发送的数据（如果此时还没有收到任何数据，它会发送一个默认值）。
     * ReplaySubject在订阅者订阅时，会发送所有的数据给订阅者，无论它们是何时订阅的。
     * AsyncSubject只在原Observable事件序列完成后，发送最后一个数据，后续如果还有订阅者继续订阅该Subject, 则可以直接接收到最后一个值。
     */
    private static volatile RxBus mInstance;
    private Subject<Object> bus;
    /**
     * ConcurrentHashMap是一个线程安全的HashMap， 采用stripping lock（分离锁），效率比HashTable高很多。
     */
    private final Map<Class<?>, Object> mStickyEventMap;


    private RxBus() {
        //PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
        //包装为线程安全的
        bus = PublishSubject.create().toSerialized();
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static RxBus getDefault() {
        if (mInstance == null) {
            synchronized (RxBus.class) {
                if (mInstance == null) {
                    mInstance = new RxBus();
                }
            }
        }
        return mInstance;
    }


    /**
     * 发送一个新事件
     */
    public void post(Object event) {
        bus.onNext(event);
    }

    /**
     * 判断是否有订阅者
     */
    public boolean hasObservable() {
        return bus.hasObservers();
    }

    /*
     * 转换为特定类型的Obserbale
     */
    public <T> Observable<T> toObservable(Class<T> type) {
        return bus.ofType(type);
    }

    /**
     * 发送粘性事件
     *
     * @param event 事件
     */
    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        post(event);
    }

    /**
     * 粘性事件的转换
     *
     * @param eventType 特定的类型
     */
    public <T> Observable<T> toObservableSticky(final Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = bus.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);

            if (event != null) {
                //merge操作符：可以将多个Observables合并，就好像它们是单个的Observable一样。
                return observable.mergeWith(Observable.create(new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> e) throws Exception {
                        e.onNext(eventType.cast(event));
                    }
                }));
            } else {
                return observable;
            }
        }
    }

    /**
     * 根据eventType获取Sticky事件
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     * 若有粘性事件订阅，app退出时，需要清理StickyMap
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }

    /*
    // 建议在Sticky时,在操作符内主动try,catch
        RxBus.getDefault().toObservableSticky(EventSticky.class)
                .flatMap(event -> {
                    return Observable.just(event)
                            // 在flatMap里变换Observable
                            .map(...)
                            // 由于下面onErrorResumeNext, 因此 error 事件无法传递到observer, 故需要在这里做处理
                            .doOnError(e -> // todo)
                            .onErrorResumeNext(Observable.never())
                })
                .subscribe(new Action1<EventSticky>() {
                    @Override
                    public void call(EventSticky eventSticky) {
                        try {
                            // 处理接收的事件

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    */

    /*
    //异常导致后续事件接收不到的处理，重新订阅方案
    private void subscribeEvent(){
        RxBus.getDefault().toObservable(Event.class)
                // 使用操作符过程中，无需try,catch，直接使用
                .subscribe(new Subscriber<Event>() {
                    @Override
                    public void onCompleted() {
                        subscribeEvent();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        subscribeEvent();
                    }

                    @Override
                    public void onNext(Event event) {
                        // 直接处理接收的事件
                    }
                });
    }*/

}
