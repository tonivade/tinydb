/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.tinydb.command.pubsub;

import static com.github.tonivade.resp.protocol.SafeString.safeString;
import static com.github.tonivade.tinydb.data.DatabaseKey.safeKey;
import static com.github.tonivade.tinydb.data.DatabaseValue.set;
import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.github.tonivade.resp.annotation.Command;
import com.github.tonivade.resp.annotation.ParamLength;
import com.github.tonivade.resp.command.Request;
import com.github.tonivade.resp.protocol.RedisToken;
import com.github.tonivade.resp.protocol.SafeString;
import com.github.tonivade.tinydb.command.TinyDBCommand;
import com.github.tonivade.tinydb.command.annotation.PubSubAllowed;
import com.github.tonivade.tinydb.command.annotation.ReadOnly;
import com.github.tonivade.tinydb.data.Database;

@ReadOnly
@Command("subscribe")
@ParamLength(1)
@PubSubAllowed
public class SubscribeCommand implements TinyDBCommand {

  private static final String SUBSCRIBE = "subscribe";
  private static final String SUBSCRIPTION_PREFIX = "subscription:";

  @Override
  public RedisToken execute(Database db, Request request) {
    Database admin = getAdminDatabase(request.getServerContext());
    String sessionId = getSessionId(request);
    Collection<SafeString> channels = getChannels(request);
    int i = channels.size();
    List<Object> result = new LinkedList<>();
    for (SafeString channel : request.getParams()) {
      admin.merge(safeKey(SUBSCRIPTION_PREFIX + channel), set(safeString(sessionId)),
          (oldValue, newValue) -> {
            Set<SafeString> merge = new HashSet<>();
            merge.addAll(oldValue.getValue());
            merge.add(safeString(sessionId));
            return set(merge);
          });
      getSessionState(request.getSession()).addSubscription(channel);
      result.addAll(asList(SUBSCRIBE, channel, i++));
    }
    return convert(result);
  }

  private String getSessionId(Request request) {
    return request.getSession().getId();
  }

  private Collection<SafeString> getChannels(Request request) {
    if (request.getParams().isEmpty()) {
      return getSessionState(request.getSession()).getSubscriptions();
    }
    return request.getParams();
  }

}
