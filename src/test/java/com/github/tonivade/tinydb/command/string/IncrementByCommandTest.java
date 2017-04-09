/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.tinydb.command.string;

import org.junit.Rule;
import org.junit.Test;

import com.github.tonivade.resp.protocol.RedisToken;
import com.github.tonivade.tinydb.command.CommandRule;
import com.github.tonivade.tinydb.command.CommandUnderTest;
import com.github.tonivade.tinydb.command.string.IncrementByCommand;

@CommandUnderTest(IncrementByCommand.class)
public class IncrementByCommandTest {

  @Rule
  public final CommandRule rule = new CommandRule(this);

  @Test
  public void testExecute() {
    rule.withParams("a", "10")
    .execute()
    .assertThat(RedisToken.integer(10));

    rule.withParams("a", "10")
    .execute()
    .assertThat(RedisToken.integer(20));

    rule.withParams("a", "5")
    .execute()
    .assertThat(RedisToken.integer(25));
  }

}
