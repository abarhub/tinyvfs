package org.tinyvfs.core.fs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.nio.file.FileStore;

import static org.junit.Assert.*;

/**
 * Created by tulip_000 on 25/07/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TVFSFileStoreTest {


    private final static Logger LOGGER = LoggerFactory.getLogger(VirtualFSProvider.class);


    private FileStore fileStore= Mockito.mock(FileStore.class);

    private TVFSFileStore tvfsFileStore;

    @Before
    public void init(){
        tvfsFileStore=new TVFSFileStore(fileStore,false);
    }


    @Test
    public void testNameOK() throws Exception {
        LOGGER.info("testNameOK");
        final String nameRef="nom1";
        when(fileStore.name()).thenReturn(nameRef);

        // methode testé
        final String name=tvfsFileStore.name();

        // vérifications
        assertEquals(nameRef,name);
        verify(fileStore).name();
        verifyNoMoreInteractions(fileStore);
    }

    @Test
    public void testTypeOK() throws Exception {

        LOGGER.info("testTypeOK");
        final String typeRef="type1";
        when(fileStore.type()).thenReturn(typeRef);

        // methode testé
        final String type=tvfsFileStore.type();

        // vérifications
        assertEquals(typeRef,type);
        verify(fileStore).type();
        verifyNoMoreInteractions(fileStore);
    }

    @Test
    public void isReadOnly() throws Exception {
    }

    @Test
    public void testGetTotalSpaceOK() throws Exception {

        LOGGER.info("testGetTotalSpaceOK");
        final long nbRef=10;
        when(fileStore.getTotalSpace()).thenReturn(nbRef);

        // methode testé
        final long nb=tvfsFileStore.getTotalSpace();

        // vérifications
        assertEquals(nbRef,nb);
        verify(fileStore).getTotalSpace();
        verifyNoMoreInteractions(fileStore);
    }

    @Test
    public void testGetUsableSpaceOK() throws Exception {

        LOGGER.info("testGetUsableSpaceOK");
        final long nbRef=15;
        when(fileStore.getUsableSpace()).thenReturn(nbRef);

        // methode testé
        final long nb=tvfsFileStore.getUsableSpace();

        // vérifications
        assertEquals(nbRef,nb);
        verify(fileStore).getUsableSpace();
        verifyNoMoreInteractions(fileStore);
    }

    @Test
    public void testGetUnallocatedSpaceOK() throws Exception {

        LOGGER.info("testGetUnallocatedSpaceOK");
        final long nbRef=17;
        when(fileStore.getUnallocatedSpace()).thenReturn(nbRef);

        // methode testé
        final long nb=tvfsFileStore.getUnallocatedSpace();

        // vérifications
        assertEquals(nbRef,nb);
        verify(fileStore).getUnallocatedSpace();
        verifyNoMoreInteractions(fileStore);
    }

    @Test
    public void supportsFileAttributeView() throws Exception {
    }

    @Test
    public void supportsFileAttributeView1() throws Exception {
    }

    @Test
    public void getFileStoreAttributeView() throws Exception {
    }

    @Test
    public void getAttribute() throws Exception {
    }

}