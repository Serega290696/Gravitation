package universe;

import java.util.List;

public interface Physics {
  void gravity(List<Atom> allAtoms, List<Atom> updatedAtoms);
  void electromagnetism(List<Atom> allAtoms, List<Atom> updatedAtoms) throws Exception;

  void nextInstant();

  Object getLock();
}
