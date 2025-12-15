// import React, { useEffect } from 'react'
// import CodeMirror from '@uiw/react-codemirror';
// import { java } from '@codemirror/lang-java';
// import { vscodeDark} from '@uiw/codemirror-theme-vscode';
// import {EditorView } from '@codemirror/view';
// // import { Decoration, ViewPlugin } from "@codemirror/view";
// // import { EditorState } from "@codemirror/state";

// const EditorPanel = ({editorHeight = "460px", setCode, code, readOnly}) => {

//   const onChange = React.useCallback((value, viewUpdate) => {
//     setCode(value);
//     // Handle code changes here, e.g., update state
//   }, []);

//   return (
//     <div id='editor' className='w-full rounded-3xl'>
//       <CodeMirror
//           value={code}
//           height={editorHeight}
//           theme={vscodeDark} 
//           extensions={[java(), EditorView.editable.of(readOnly)]}
//           onChange={onChange}
//           // readOnly={"nocursor"}
//         />
//     </div>
//   )
// }

// export default EditorPanel


// // , EditorView.editable.of(false)




import React, { useEffect, useMemo } from 'react';
import CodeMirror from '@uiw/react-codemirror';
import { java } from '@codemirror/lang-java';
import { vscodeDark } from '@uiw/codemirror-theme-vscode';
import { EditorView, Decoration, ViewPlugin, DecorationSet } from '@codemirror/view';
import { RangeSetBuilder } from '@codemirror/state';

const EditorPanel = ({ 
  editorHeight = "460px", 
  setCode, 
  code, 
  readOnly = false,
  currentLine = null,
  previousLine = null 
}) => {

  const onChange = React.useCallback((value, viewUpdate) => {
    if (setCode) {
      setCode(value);
    }
  }, [setCode]);

  try {
    
    setCode(code);
  } catch (error) {
    
  }

  // Create line highlighting extension
  const lineHighlighter = useMemo(() => {
    if (!currentLine && !previousLine) return [];

    return ViewPlugin.fromClass(
      class {
        constructor(view) {
          this.decorations = this.buildDecorations(view);
        }

        update(update) {
          if (update.docChanged || update.viewportChanged) {
            this.decorations = this.buildDecorations(update.view);
          }
        }

        buildDecorations(view) {
          const builder = new RangeSetBuilder();
          
          for (let { from, to } of view.visibleRanges) {
            for (let pos = from; pos <= to; ) {
              const line = view.state.doc.lineAt(pos);
              const lineNumber = line.number;
              
              // Highlight current line (yellow)
              if (currentLine && lineNumber === currentLine) {
                builder.add(
                  line.from,
                  line.from,
                  Decoration.line({
                    attributes: { 
                      class: 'cm-current-line-highlight',
                      style: 'background-color: rgba(250, 204, 21, 0.15); border-left: 3px solid #facc15;'
                    }
                  })
                );
              }
              
              // Highlight previous line (blue)
              if (previousLine && lineNumber === previousLine && previousLine !== currentLine) {
                builder.add(
                  line.from,
                  line.from,
                  Decoration.line({
                    attributes: { 
                      class: 'cm-previous-line-highlight',
                      style: 'background-color: rgba(59, 130, 246, 0.15); border-left: 3px solid #3b82f6;'
                    }
                  })
                );
              }
              
              pos = line.to + 1;
            }
          }
          
          return builder.finish();
        }
      },
      {
        decorations: v => v.decorations
      }
    );
  }, [currentLine, previousLine]);

  const extensions = useMemo(() => {
    const exts = [
      java(),
      EditorView.editable.of(!readOnly)
    ];
    
    if (currentLine || previousLine) {
      exts.push(lineHighlighter);
    }
    
    return exts;
  }, [readOnly, lineHighlighter, currentLine, previousLine]);

  return (
    <div id='editor' className='w-full rounded-lg overflow-hidden'>
      <CodeMirror
        value={code}
        height={editorHeight}
        theme={vscodeDark}
        extensions={extensions}
        onChange={onChange}
        basicSetup={{
          lineNumbers: true,
          highlightActiveLineGutter: false,
          highlightActiveLine: false,
          foldGutter: true,
          dropCursor: true,
          allowMultipleSelections: true,
          indentOnInput: true,
          bracketMatching: true,
          closeBrackets: true,
          autocompletion: true,
          rectangularSelection: true,
          crosshairCursor: true,
          highlightSelectionMatches: true,
          closeBracketsKeymap: true,
          searchKeymap: true,
          foldKeymap: true,
          completionKeymap: true,
          lintKeymap: true,
        }}
      />
    </div>
  );
};

export default EditorPanel;