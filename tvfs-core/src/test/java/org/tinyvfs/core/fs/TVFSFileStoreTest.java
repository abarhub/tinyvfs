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
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;

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
    public void testIsReadOnlyOK() throws Exception {

        LOGGER.info("testIsReadOnlyOK");
        final boolean readonlyRef=true;
        tvfsFileStore=new TVFSFileStore(fileStore,readonlyRef);

        //methode testé
        final boolean readonly=tvfsFileStore.isReadOnly();

        //vérifications
        assertEquals(readonlyRef,readonly);
        verifyNoMoreInteractions(fileStore);
    }

    @Test
    public void testIsReadOnly2OK() throws Exception {

        LOGGER.info("testIsReadOnly2OK");
        final boolean readonlyRef=false;
        tvfsFileStore=new TVFSFileStore(fileStore,readonlyRef);

        //methode testé
        final boolean readonly=tvfsFileStore.isReadOnly();

        //vérifications
        assertEquals(readonlyRef,readonly);
        verifyNoMoreInteractions(fileStore);
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
    public void testSupportsFileAttributeViewOK() throws Exception {

        LOGGER.info("testSupportsFileAttributeViewOK");

        // methode testé
        tvfsFileStore.supportsFileAttributeView(BasicFileAttributeView.class);

        // vérifications
        verify(fileStore).supportsFileAttributeView(eq(BasicFileAttributeView.class));
        verifyNoMoreInteractions(fileStore);
    }

    @Test
    public void testSupportsFileAttributeView1OK() throws Exception {
        LOGGER.info("testSupportsFileAttributeView1OK");

        final String attr="attr1";

        // methode testé
        tvfsFileStore.supportsFileAttributeView(attr);

        // vérifications
        verify(fileStore).supportsFileAttributeView(eq(attr));
        verifyNoMoreInteractions(fileStore);
    }

    @Test
    public void testGetFileStoreAttributeViewOK() throws Exception {

        LOGGER.info("testGetFileStoreAttributeViewOK");
        final FileStoreAttributeView valRef=new FileStoreAttributeView(){
            @Override
            public String name() {
                return "attr456";
            }
        };
        when(fileStore.getFileStoreAttributeView(FileStoreAttributeView.class)).thenReturn(valRef);

        // methode testé
        final FileStoreAttributeView val=tvfsFileStore.getFileStoreAttributeView(FileStoreAttributeView.class);

        // vérifications
        assertEquals(valRef,val);
        verify(fileStore).getFileStoreAttributeView(eq(FileStoreAttributeView.class));
        verifyNoMoreInteractions(fileStore);
    }

    @Test
    public void testGetAttributeOK() throws Exception {

        LOGGER.info("testGetAttributeOK");
        final Integer valRef=8 ;
        final String nomAttr="attr789";
        when(fileStore.getAttribute(nomAttr)).thenReturn(valRef);

        // methode testé
        final Object val=tvfsFileStore.getAttribute(nomAttr);

        // vérifications
        assertEquals(valRef,val);
        verify(fileStore).getAttribute(eq(nomAttr));
        verifyNoMoreInteractions(fileStore);
    }

}