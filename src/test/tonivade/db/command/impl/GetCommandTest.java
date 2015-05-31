package tonivade.db.command.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static tonivade.db.data.DatabaseValue.string;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import tonivade.db.data.DataType;
import tonivade.db.data.DatabaseValue;

@CommandUnderTest(GetCommand.class)
public class GetCommandTest {

    @Rule
    public final CommandRule rule = new CommandRule(this);

    @Captor
    private ArgumentCaptor<DatabaseValue> captor;

    @Test
    public void testExecute() {
        rule.withData("key", string("value"))
            .withParams("key")
            .execute()
            .verify().addValue(captor.capture());

        DatabaseValue value = captor.getValue();

        assertThat(value.getType(), is(DataType.STRING));
        assertThat(value.getValue(), is("value"));
    }

}