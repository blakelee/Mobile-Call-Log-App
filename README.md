# Call Log

A sample app showing a list of all your calls. Handles permissions gracefully.

## Checklist

* Is the app responsive? ✔
* Is the code modular? ✔
* Is the code following all the best practices? **Using single activity but not fragments. No DI library used
* Have you run a lint tool? ✔ Some warnings but nothing weird

## Questions

* What are the libraries or frameworks you have chosen? and why?
```
RxJava was so that I can make everything reactive
RxAndroid so I can use the AndroidSchedulers.mainThread()
RxRelay because I wanted a BehaviorSubject without the exceptions
RecyclerView because ListView doesn't recycle the view
ConstraintLayout because it's recommended over RelativeLayout, more versatile, and faster in most cases
```

* If you had more time, what would you have done better?
```
I am happy with the reactive result seen in CallLogRepository but testing it is a nightmare so I chose not to.

If I really wanted to make it slick, whenever a new call log item popped up, instead of getting the whole list,
I could just get the most recent item and put it in the recyclerview. That way I can have the animation of the
new item being inserted.

If the app wasn't so tied to BrodcastReceiver and the ContentResolver, I would have a separate module called
model and would stick everything non-Android related in there such as anything dealing with REST API's. By doing
that we can't accidentally use any Android stuff in places we're not supposed to and it also will speed up
compile time if you only change stuff in the app module and not the model module.

As far as dependency injection goes, it's there, but it's not using any fancy libraries. I would try Koin if I
had more time.
```

* What are some key architecture considerations?
```
The basic flow is view -> viewmodel -> model

The view should essentially remain dumb. It should not attempt to do any logic if possible. All of the logic of
what views should say or do should be coming from the viewmodel. The idea is to have the majority of the logic in
theviewmodel because the viewmodel can be unit tested whereas the view needs instrumentation tests. Having a view
that subscribes to stuff in the viewmodel also helps with configuration changes. If the viewmodel exists
independent of the view, then when the view is destroyed and recreated, the viewmodel sticks around so that you
can easily get the current state. No memory leaks :)

You'll also notice I didn't use any DI frameworks such as Koin or Dagger II. This was just so I can create
everything quicker instead of toying with them for who knows how long.

I also created several interfaces so that stuff can be mocked pretty easily. However, because so much was tied
to the android system, I just skipped it altogether.

I did make a MockCallLogRespository the could be used to simulate multiple new call logs coming in. I have used
a mock model in the past to simulate an entire flow with all the events that would be expected in the real world.

Another key consideration was observing the call log. The idea is that we only register the receiver when
something is subscribed to it. When that subscription is disposed, we unregister the receiver. If this was used
in multiple places I would consider changing the function to a private val, adding a share() to the end of it,
and the observeCallLog() function would return the val we created. That way it doesn't register many receivers
and just keeps the one.
```