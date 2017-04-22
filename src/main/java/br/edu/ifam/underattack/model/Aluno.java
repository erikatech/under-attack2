package br.edu.ifam.underattack.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by erika.silva on 22/04/2017.
 */

@Entity
@DiscriminatorValue(value = "Aluno")
public class Aluno extends Usuario {




}
