package tonivade.db.command.impl;

import tonivade.db.command.ICommand;
import tonivade.db.command.IRequest;
import tonivade.db.command.IResponse;
import tonivade.db.data.IDatabase;

/**
 *
 * @author tomby
 *
 */
public class GetCommand implements ICommand {

    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        if (request.getLength() < 2) {
            response.addError(ERROR);
        } else {
            response.addValue(db.get(request.getParam(1)));
        }
    }

}
