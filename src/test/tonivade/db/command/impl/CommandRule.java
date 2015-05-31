package tonivade.db.command.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import tonivade.db.command.CommandWrapper;
import tonivade.db.command.ICommand;
import tonivade.db.command.IRequest;
import tonivade.db.command.IResponse;
import tonivade.db.data.Database;
import tonivade.db.data.DatabaseValue;
import tonivade.db.data.IDatabase;

public class CommandRule implements TestRule {

    private IRequest request;

    private IResponse response;

    private IDatabase database;

    private final Object target;

    private ICommand command;

    public CommandRule(Object target) {
        super();
        this.target = target;
    }

    public IRequest getRequest() {
        return request;
    }

    public IResponse getResponse() {
        return response;
    }

    public IDatabase getDatabase() {
        return database;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                request = mock(IRequest.class);
                response = mock(IResponse.class);
                database = new Database(new HashMap<String, DatabaseValue>());

                MockitoAnnotations.initMocks(target);

                command = target.getClass().getAnnotation(CommandUnderTest.class).value().newInstance();

                base.evaluate();

                database.clear();
            }
        };
    }

    public CommandRule withData(String key, DatabaseValue value) {
        database.put(key, value);
        return this;
    }

    public CommandRule execute() {
        new CommandWrapper(command).execute(database, request, response);
        return this;
    }

    public CommandRule withParams(String ... params) {
        if (params != null) {
            when(request.getParams()).thenReturn(Arrays.asList(params));
            int i = 0;
            for (String param : params) {
                when(request.getParam(i++)).thenReturn(param);
            }
            when(request.getLength()).thenReturn(params.length);
        }
        return this;
    }

    public CommandRule assertThat(String key, Matcher<DatabaseValue> matcher) {
        Assert.assertThat(database.get(key), matcher);
        return this;
    }

    public IResponse verify() {
        return Mockito.verify(response);
    }

}