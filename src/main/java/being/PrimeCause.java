package being;

import being.exceptions.UniverseCreationException;
import being.universe.AbstractUniverse;
import being.universe.UniverseType;

public class PrimeCause {

    public static void main(String[] args) {
        while (!God.ONE.isOblivion()) {
            new PrimeCause();
        }
    }

    // private модификатор изящно намекает, что первопричина не может быть создана кем то, ведь у нее нет истоков...... .-.
// private modifier delicately hinted that the prime cause can not be created by someone, because it has no cause...... .-.
    private PrimeCause() {

// Почему вселенная №42 ? Ведь именно поэтому 42 это ответ на главный вопрос жизни вселенной и всего такого. (Из  "Автостопом по галактике")
// Why being #42 ? Because 42 is the answer to the Ultimate Question of Life, the Universe and Everything. "The Hitchhiker's Guide to the Galaxy"
        final AbstractUniverse universe;
        try {
            universe = God.ONE.sayLetThereBeLight(UniverseType.UNIVERSE_42);
            new Thread(God.ONE.MIND::bindToUserMind).start();
            universe.bigBang();
//            universe.live();
        } catch (UniverseCreationException e) {
            e.printStackTrace();
        }
    }
}
