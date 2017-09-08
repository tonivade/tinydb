/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.tinydb.command.pubsub;

import static com.github.tonivade.resp.protocol.RedisToken.integer;

import com.github.tonivade.resp.annotation.Command;
import com.github.tonivade.resp.annotation.ParamLength;
import com.github.tonivade.resp.command.Request;
import com.github.tonivade.resp.protocol.RedisToken;
import com.github.tonivade.resp.protocol.SafeString;
import com.github.tonivade.tinydb.TinyDBServerContext;
import com.github.tonivade.tinydb.command.TinyDBCommand;
import com.github.tonivade.tinydb.data.Database;

@Command("publish")
@ParamLength(2)
public class PublishCommand extends SubscriptionManager implements TinyDBCommand {

  @Override
  public RedisToken execute(Database db, Request request) {
    String channel = request.getParam(0).toString();
    SafeString message = request.getParam(1);
    return integer(publishAll(getTinyDB(request.getServerContext()), channel, message));
  }

  private int publishAll(TinyDBServerContext tinyDB, String channel, SafeString message) {
    int count = publish(tinyDB, channel, message);
    int pcount = patternPublish(tinyDB, channel, message);
    return count + pcount;
  }
}
