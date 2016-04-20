# File-Synchronisation-Software
File-Synchronisation-Software based Java


Design and realisation of a directory synchroni- sation tool
The project aims at the realisation of a GUI based tool for synchronising the contents of two directories (another word for “folders”). Given two directories, say, A and B, the standard operation will make sure that the contents of direc- tory B become identical to the contents of directory A (as well as – recursively – its subdirectories). This is done with a minimum of file operations – i.e. if two files under the directories are already identical (meaning that file names, as well as contents are equal) nothing happens with these files. When a synchronisation tool is invoked, it will typically first propose a series of synchronisation actions that need to be acknowledged by the user before the actual synchronisation is triggered.
The tool is supposed to be developed in Java – version 1.5 or later. The GUI should minimally allow the user to:
• specify which two directories need to be synchronised and which is their role – in the example given above, directory A is the source directory (which stays unaffected) and B is the target directory; note that this specification should be written to a “control file”, so that in a later invo- cation of the synchronisation tool the user doesn’t need to re-enter this information,
• inverse the source/target roles of the specified synchronised directories,
• visualise the contents of both directories, with next to the name of each
file (subdirectory) the planned synchronisation action:
create: holds for a file or subdirectory that is present in the source di- rectory and absent in the target directory,
update: holds for a file that is present in both source and target (sub-) directories and where the most recent version is the one of the source (sub-) directory,
1
restore: holds for a file that is present in both source and target (sub-) directories and where the most recent version is the one of the target (sub-) directory,
delete: holds for a file or subdirectory that is absent in the source direc- tory and present in the target directory,
• specify for each file individally whether the suggested action needs to be performed or not during synchronisation,
• to trigger the synchronisation.
