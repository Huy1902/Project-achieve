package threeoone.bigproject.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A component used to send requests.
 * Multiple instances of this class are registered to {@code Spring} context,
 * each corresponds to one type of request.
 * <p>
 * When a request is sent, components that have registered to the {@link RequestSender} are
 * notified and have their mapped actions executed.
 * Components are registered by {@link #registerReceiver}.
 *
 * @param <T> the request body, containing necessary data
 *
 * @author DUCBRICK
 */
public class RequestSender <T> {
  private final List <Consumer <T>> receivers = new ArrayList <> ();

  /**
   * Register a component to the {@link RequestSender} and to its type of request.
   *
   * @param receiver method that is called when a request of this type is sent,
   *                 with the request body as parameter
   */
  public void registerReceiver(Consumer <T> receiver) {
    receivers.add(receiver);
  }

  /**
   * Send a request corresponding to the {@link RequestSender}.
   *
   * @param requestBody the body of the request, containing necessary data
   */
  public void send(T requestBody) {
    for (Consumer <T> receiver : receivers) {
      receiver.accept(requestBody);
    }
  }
}
