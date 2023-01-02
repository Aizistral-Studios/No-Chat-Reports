package com.aizistral.nochatreports.common.platform.events;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class PlatformEvent<T> {
	private final Collection<T> callbacks;
	private final Function<Collection<T>, T> invokerFactory;
	private volatile T invoker;

	private PlatformEvent(Function<Collection<T>, T> invokerFactory) {
		this.callbacks = new LinkedList<>();
		this.invokerFactory = invokerFactory;
	}

	public T invoker() {
		return this.invoker;
	}

	public void register(T callback) {
		this.callbacks.add(callback);
		this.update();
	}

	private void update() {
		this.invoker = this.invokerFactory.apply(this.callbacks);
	}

	static <T> PlatformEvent<T> create(Function<Collection<T>, T> invokerFactory) {
		return new PlatformEvent<>(invokerFactory);
	}

}
