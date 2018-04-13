package net.coding.mart.common.util;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by chenchao on 16/6/23.
 */
public class RxBus {

    private static RxBus sBus = new RxBus();

    public static RxBus getInstance() {
        return sBus;
    }

    private RxBus() {}

    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }

    public static class UpdateMainEvent {}

    public static class RewardPublishSuccess {}

    public static class UpdateBottomBarEvent {}

    public static class UpdateBottomBarMessageEvent {}
}
