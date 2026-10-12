package de.fh.swf.notenverwaltung.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.fh.swf.notenverwaltung.entity.Kolloqium;
import de.fh.swf.notenverwaltung.entity.Student;
import de.fh.swf.notenverwaltung.repository.KolloqiumRepository;
import de.fh.swf.notenverwaltung.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class KolloqiumServiceTest {

	@Mock
	StudentRepository studentRepository;

	@Mock
	KolloqiumRepository kolloqiumRepository;

	@InjectMocks
	KolloqiumService kolloqiumService;

	private Student student = new Student("lennard", "langenbruch");

	@Test
	void createKolloqium_ohneVorhandenesKolloqium_wirdAngelegtUndVerknuepft() {
		when(studentRepository.findOneByNachname("langenbruch")).thenReturn(student);
		when(kolloqiumRepository.findOneByThema("Verteidigung")).thenReturn(null);

		kolloqiumService.createKolloqium("Verteidigung", "Beschreibung", "1.0", "");

		ArgumentCaptor<Kolloqium> captor = ArgumentCaptor.forClass(Kolloqium.class);
		verify(kolloqiumRepository).save(captor.capture());
		Kolloqium k = captor.getValue();
		assertEquals("Verteidigung", k.getThema());
		assertEquals("Beschreibung", k.getBeschreibung());
		assertTrue(k.getBestanden());
		assertEquals("1.0", k.getEndnote());
		assertSame(student, k.getStudent());
		assertSame(k, student.getKolloqium());
	}

	@Test
	void createKolloqium_studentHatBereitsKolloqium_speichertNichts() {
		student.setKolloqium(new Kolloqium("Altes Thema"));
		when(studentRepository.findOneByNachname("langenbruch")).thenReturn(student);

		kolloqiumService.createKolloqium("Verteidigung", "Beschreibung", "1.0", "");

		verify(kolloqiumRepository, never()).save(any());
	}

	@Test
	void deleteKolloqium_loeschtUndEntferntVerknuepfung() {
		Student s = mock(Student.class);
		when(studentRepository.findOneByNachname("langenbruch")).thenReturn(s);

		kolloqiumService.deleteKolloqium(3L);

		verify(kolloqiumRepository).deleteById(3L);
		verify(s).setKolloqium(null);
		verify(s).berechnen();
	}

	@Test
	void findAll_liefertAlleKolloquien() {
		List<Kolloqium> kolloquien = List.of(new Kolloqium("Verteidigung"));
		when(kolloqiumRepository.findAll()).thenReturn(kolloquien);

		assertEquals(kolloquien, kolloqiumService.findAll());
	}

}
