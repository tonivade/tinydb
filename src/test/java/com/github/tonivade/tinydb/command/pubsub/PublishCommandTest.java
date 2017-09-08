/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.tinydb.command.pubsub;

import static com.github.tonivade.resp.protocol.RedisToken.array;
import static com.github.tonivade.resp.protocol.RedisToken.string;
import static com.github.tonivade.tinydb.DatabaseValueMatchers.set;

import org.junit.Rule;
import org.junit.Test;

import com.github.tonivade.resp.protocol.RedisToken;
import com.github.tonivade.tinydb.TinyDBServerContext;
import com.github.tonivade.tinydb.command.CommandRule;
import com.github.tonivade.tinydb.command.CommandUnderTest;

@CommandUnderTest(PublishCommand.class)
public class PublishCommandTest {

  @Rule
  public final CommandRule rule = new CommandRule(this);

  @Test
  public void publish()  {
    rule.withAdminData("subscription:test", set("localhost:12345"))
    .withParams("test", "Hello World!")
    .execute()
    .assertThat(RedisToken.integer(1))
    .verify(TinyDBServerContext.class).publish("localhost:12345",
        array(string("message"), string("test"), string("Hello World!")));
  }

  @Test
  public void publishPattern() {
    rule.withAdminData("psubscription:test:*", set("localhost:12345"))
    .withParams("test:pepe", "Hello World!")
    .execute()
    .assertThat(RedisToken.integer(1))
    .verify(TinyDBServerContext.class).publish("localhost:12345",
         array(string("pmessage"), string("test:*"), string("test:pepe"), string("Hello World!")));
  }

}
