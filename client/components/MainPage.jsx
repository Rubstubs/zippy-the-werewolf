import React from "react";
import zippy from "../public/zippy.PNG";

export function MainPage() {
    return(
        <div>
            <h1 id="zippy-title">Give Zippy some files, if you dare!</h1>
            <div className="zippy-img-container">
                <img id="zippy-img" src={zippy} alt="zippy, the werewolf"/>
                <i id="file-icon" className="bi bi-card-checklist"/>
            </div>
            <div>
                <form className="form" method="POST" action="/" encType="multipart/form-data" id="fileUploadForm">
                    <div className="choose-file-container">
                        <input onChange={() => document.getElementById("file-icon").classList.toggle("clicked")} type="file" placeholder="Upload and zip your file" multiple name="files"/>
                    </div>
                    {/*<label htmlFor="email">Your email</label>*/}
                    <input placeholder="Your email" type="email" name="email"/>
                    <button>Go forth</button>
                </form>
            </div>
        </div>
    );
}