/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.tinydb.command.server;

import static com.github.tonivade.resp.protocol.RedisToken.status;
import static com.github.tonivade.tinydb.data.DatabaseValue.string;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.github.tonivade.tinydb.command.CommandRule;
import com.github.tonivade.tinydb.command.CommandUnderTest;

@CommandUnderTest(FlushDBCommand.class)
public class FlushDBCommandTest {

  @Rule
  public final CommandRule rule = new CommandRule(this);

  @Test
  public void testExecute() {
    rule.withData("a", string("test"))
    .execute()
    .assertThat(status("OK"));

    assertThat(rule.getDatabase().isEmpty(), is(true));
  }

}
